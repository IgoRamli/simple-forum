import { fetchForum } from "@/app/lib/data";
import ForumList from "@/app/ui/components/forum-list";
import SearchHeader from "./search-header";
import { getSessionSSR } from "@/app/lib/auth";

export default async function ForumsPage({
    searchParams,
  }: {
    searchParams?: { query?: string; }
  }) {
    try {
      const session = await getSessionSSR();
      if (!session || session.error) {
        return <div className="p-6">
          <p>Please log in to view forums</p>
        </div>
      }
      const query = (searchParams?.query) ? searchParams?.query : "";
      const posts = await fetchForum(query, session!);
    
      return (
        <div className="flex flex-col h-full w-full p-6">
          <SearchHeader session={session!} />
          <ForumList data={posts} />
        </div>
      )
    } catch (e) {  // TODO: Probably use error.js for this. See https://nextjs.org/docs/app/building-your-application/routing/error-handling 
      console.error(`Failed to render ForumsPage: ${e}`)
      return (
        <div className="p-6">
          <p>Unable to load forums</p>
        </div>
      )
    }
}