import { Session } from "next-auth";

export interface GetPostsResponse {
  id: number,
  content: string,
  title: string,
  createdAt: string,
  updatedAt: string,
}

export async function fetchForum(searchQuery: string, session: Session) : Promise<Array<GetPostsResponse>> {
  try {
    const postsList = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/posts?keyword=${searchQuery}`, {
      headers: {
        'Authorization': getBearerToken(session),
      }
    });
    return postsList.json();
  } catch (error) {
    console.error(`Failed to fetch forum posts : ${error}`);
    throw new Error(`Failed to fetch forum posts : ${error}`);
  }
}

export async function createPost(formData: FormData, session: Session) {
  try {
    const result = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/posts`, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': getBearerToken(session),
      },
      body: JSON.stringify({
        'title': formData.get('title'),
        'content': formData.get('content'),
      })
    })
    if (!result.ok) {
      const errMsg = `New post submission returns error ${result.status} : ${result.json()}`
      console.error(errMsg);
      throw new Error(errMsg);
    }
  } catch (error) {
    const errMsg = `Failed to submit new post : ${error}`;
    console.error(errMsg);
    throw new Error(errMsg);
  }
}

function getBearerToken(session: Session) {
  return `Bearer ${session.accessToken}`;
}