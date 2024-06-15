import { GetPostsResponse } from "@/./app/lib/data"
import { Suspense } from 'react';
import PostCard from "@/./app/ui/components/postcard";

export default function ForumList({ data } : { data: GetPostsResponse[] }) {
  return (
    <Suspense fallback={<div><p>Loading forums...</p></div>}>
      {data.map((element) => {
        return <PostCard key={element.id} title={element.title} content={element.content} createdAt={element.createdAt} updatedAt={element.updatedAt} />
      })}
    </Suspense>
  )
}