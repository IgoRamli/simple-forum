'use client'

import Button from "@/app/ui/components/button"
import SearchBar from "@/app/ui/components/search-bar"
import { useState } from "react"
import NewPostForm from "@/app/ui/components/new-post-form";
import { Session } from "next-auth";
import { useSearchParams, usePathname, useRouter } from 'next/navigation';

export default function SearchHeader({ session } : { session : Session }) {
  const [newPostFormEnabled, setNewPostFormEnabled] = useState(false);
  const searchParams = useSearchParams();
  const pathname = usePathname();
  const { replace } = useRouter();

  function handleSearch(term: string) {
    const params = new URLSearchParams(searchParams);
    if (term) {
      params.set('query', term);
    } else {
      params.delete('query');
    }
    replace(`${pathname}?${params.toString()}`);
  }

  return <div>
    <div className="flex flex-col md:flex-row mb-4">
      <SearchBar onInputChange={handleSearch}/>
      <Button className="" onClick={() => {setNewPostFormEnabled(!newPostFormEnabled)}} text="New Post"/>
    </div>
    { newPostFormEnabled ? <NewPostForm onSubmitSuccess={() => setNewPostFormEnabled(false)} session={session} /> : <></>}
  </div>
}