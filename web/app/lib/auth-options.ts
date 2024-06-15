import { AuthOptions, User } from "next-auth";
import KeycloakProvider from "next-auth/providers/keycloak"
import { JWT } from "next-auth/jwt";

export const authOptions : AuthOptions = {
  providers: [
    KeycloakProvider({
      clientId: process.env.KEYCLOAK_CLIENT_ID,
      clientSecret: process.env.KEYCLOAK_CLIENT_SECRET,
      issuer: process.env.KEYCLOAK_ISSUER
    })
  ],
  secret: process.env.NEXTAUTH_SECRET,
  session: { 
    strategy: "jwt"
  },
  callbacks: {
    async session({ session, token }) {  // Note: This requires SessionProvider put inside root layout to work
      session.accessToken = token.accessToken;
      session.expiresAt = token.expiresAt;
      session.refreshToken = token.refreshToken;
      session.user = token.user;
      session.error = token.error;
      return session;
    },
    async jwt({ token, account }): Promise<JWT> {
      if (account) {
        const userProfile: User = {
          id: token.sub!,
          name: token.name,
          email: token.email,
          image: token?.picture,
        };

        return {
          accessToken: account.access_token!,
          expiresAt: account.expires_at!,  // Expiry timestamp in seconds
          refreshToken: account.refresh_token!,
          user: userProfile,
        };
      } else if (token.expiresAt * 1000 < Date.now()) {
        try {
          const newToken = await refreshSession(token.refreshToken);
          return {
            accessToken: newToken.access_token as string,
            expiresAt: Math.floor(Date.now() / 1000 + newToken.expires_in) as number,
            refreshToken: newToken.refresh_token as string,
            user: token.user,
          };
        } catch (e) {
          console.log(`Error when refreshing access token: ${e}`);
          return { ...token, error: "RefreshAccessTokenError" as const }
        }
      }
      return token;
    },
  }
}

async function refreshSession (refreshToken: string): Promise<RefreshTokenResponse> {
  // Refresh token
  console.log("Token expired. Refreshing token...");
  const request = new Request(`${process.env.KEYCLOAK_BASE_URL}/realms/${process.env.KEYCLOAK_REALM}/protocol/openid-connect/token`, {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: getUrlencodedRequestBody({
      'client_id': process.env.KEYCLOAK_CLIENT_ID,
      'client_secret': process.env.KEYCLOAK_CLIENT_SECRET,
      'refresh_token': refreshToken,
      'grant_type': 'refresh_token',
    }),
  });

  return fetch(request)
    .then((response) => {
      if (response.status === 200) {
        return response.json()
      } else {
        throw new Error(`Failed to log in using refresh token : ${response.json()}, Status code ${response.status}`)
      }
  })
}

function getUrlencodedRequestBody(body: Object): string {
  const formBody = [];
  for (const [key, value] of Object.entries(body)) {
    var encodedKey = encodeURIComponent(key);
    var encodedValue = encodeURIComponent(value);
    formBody.push(encodedKey + "=" + encodedValue);
  }
  return formBody.join("&");
}

declare module "next-auth" {
  /**
   * Returned by `useSession`, `getSession` and received as a prop on the `SessionProvider` React Context
   */
  interface Session {
    accessToken: string,
    expiresAt: number,
    refreshToken: string,
    error?: "RefreshAccessTokenError",
  }
}

declare module "next-auth/jwt" {
  interface JWT {
    accessToken: string,
    refreshToken: string,
    expiresAt: number,
    user: User,
    error?: "RefreshAccessTokenError",
  }
}

interface RefreshTokenResponse {
  access_token: string,
  expires_in: number,
  refresh_expires_in: number,
  refresh_token: string,
  token_type: string,
  session_state: string,
  scope: string
}