declare namespace NodeJS {
  export interface ProcessEnv {
    NEXT_PUBLIC_API_BASE_URL: string,
    NEXT_PUBLIC_KEYCLOAK_BASE_URL: string,
    KEYCLOAK_CLIENT_ID: string,
    KEYCLOAK_CLIENT_SECRET: string,
    KEYCLOAK_REALM: string,
    KEYCLOAK_ISSUER: string,
    NEXTAUTH_SECRET: string,
  }
}