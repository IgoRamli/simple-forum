'use client';

import { GetPostsResponse, fetchForum } from "../lib/data";
import NewPostForm from "../ui/components/new-post-form";
import { useEffect, useState } from "react";
import SearchBar from "@/app/ui/components/search-bar";
import Button from "../ui/components/button";
import ForumList from "../ui/components/forum-list";
import { getSessionCSR } from "../lib/auth";

export default function ForumsPage() {
  const [query, setQuery] = useState("");
  const [posts, setPosts] = useState([] as GetPostsResponse[]);
  const [newPostFormEnabled, setNewPostFormEnabled] = useState(false);
  const session = getSessionCSR();

  useEffect(() => {
    if (session && !session.error) {
      fetchForum(query, session!).then(setPosts);
    }
  }, [session, query, newPostFormEnabled]);

  return (
    <div className="flex flex-col h-full w-full p-6">
      <div className="flex flex-col md:flex-row mb-4">
        <SearchBar onInputChange={setQuery}/>
        <Button className="" onClick={() => setNewPostFormEnabled(!newPostFormEnabled)} text="New Post"/>
      </div>
      { newPostFormEnabled ? <NewPostForm onSubmitSuccess={() => setNewPostFormEnabled(false)} session={session!} /> : <></>}
      <ForumList data={posts} />
    </div>
  )
}