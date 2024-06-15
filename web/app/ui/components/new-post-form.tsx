"use client";

import { useState } from "react";
import Button from "./button";
import { createPost } from "@/app/lib/data";
import { useRouter } from "next/navigation";
import { Session } from "next-auth";

export default function NewPostForm({ onSubmitSuccess, session } : { onSubmitSuccess : () => void , session : Session | null }) {
  const router = useRouter();
  const [submissionStatus, setSubmissionStatus] = useState((session) ? "ready" : "unauthenticated");  // Possible values: "unauthenticated", "ready", "in-progress", "error"
  const [submissionError, setSubmissionError] = useState("");

  async function submitNewPost(formData: FormData) {
    setSubmissionStatus("in-progress");
    createPost(formData, session!)
      // .then(() => new Promise(res => setTimeout(res, 3000)))  // Uncomment to test slow form submission
      .then(() => setSubmissionStatus("ready"))
      .then(onSubmitSuccess)
      .then(router.refresh)  // Refetch posts lists
      .catch((e) => {
        setSubmissionStatus("error")
        setSubmissionError(e.message)
      })
  }

  return (
    <div className="flex flex-col">
      <p className="mb-4">Write your new post here!</p>
      <form className="w-full" action={submitNewPost}>
        <input className="w-full mb-2 p-4 text-md text-gray-900 border border-gray-300 rounded-lg bg-gray-50 focus:ring-blue-500 focus:border-blue-500" type="text" name="title" placeholder="Post Title" />
        <textarea className="w-full mb-2 p-4 text-sm text-gray-900 border border-gray-300 rounded-lg bg-gray-50 focus:ring-blue-500 focus:border-blue-500" name="content" cols={40} rows={5} placeholder="Write down your thoughts..." />
        <div className="flex flex-row mb-4 items-center">
          <Button className="w-fit" text="Create New Post"/>
          <SubmissionStatus status={submissionStatus} errMsg={submissionError}/>
        </div>
      </form>
    </div>
  )
}

function SubmissionStatus({ status, errMsg } : { status : string, errMsg: string }) {
  switch (status) {
    case "in-progress":
      return <p className="mx-4 text-sm">Sending...</p>
    case "error":
      return <p className="mx-4 text-sm text-red">Error with submitting new form: {errMsg}</p>
    case "unauthenticated":
      return <p className="mx-4 text-sm text-red">You need to log in before submitting any posts</p>
    default:
      return <></>
  }
}