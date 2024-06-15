import Login from '@/app/ui/components/login'
import Logout from '@/app/ui/components/logout'
import { getSessionSSR } from '../lib/auth';

export default async function Home() {
  const session = await getSessionSSR();
  if (session && !session.error) {
    return <div>
      <div>Welcome, {session.user?.name}</div>
      <div><Logout /></div>
    </div>
  }
  return (
    <div>
      <Login />
    </div>
  )
}