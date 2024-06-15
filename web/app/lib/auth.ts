import type {
  GetServerSidePropsContext,
  NextApiRequest,
  NextApiResponse,
} from "next"
import type { Session } from "next-auth"
import { getServerSession } from "next-auth"
import { useSession } from "next-auth/react"
import { authOptions } from "./auth-options"

// You'll need to import and pass this
// to `NextAuth` in `app/api/auth/[...nextauth]/route.ts`

// Use it in server contexts
export function getSessionSSR(
  ...args:
    | [GetServerSidePropsContext["req"], GetServerSidePropsContext["res"]]
    | [NextApiRequest, NextApiResponse]
    | []
) {
  return getServerSession(...args, authOptions)
}

export function getSessionCSR() : Session | null {
  const { data, status } = useSession();
  if (status === "authenticated") {
    return data;
  }
  return null;
}