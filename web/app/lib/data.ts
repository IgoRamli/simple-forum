import { Session } from "next-auth";

const GET_POSTS_BATCH_SIZE = 50;

export interface GetPostsResponse {
  id: number,
  content: string,
  title: string,
  createdAt: string,
  updatedAt: string,
}

export async function fetchForum(searchQuery: string, session: Session) : Promise<Array<GetPostsResponse>> {
  try {
    const postsList = [] as Array<GetPostsResponse>;
    let lastId = undefined;
    while (true) {
      const batch = await fetchBatchOfPosts(session, searchQuery, GET_POSTS_BATCH_SIZE, lastId);
      postsList.push(...batch);
      if (batch.length < GET_POSTS_BATCH_SIZE) {
        break;
      }
      lastId = batch[batch.length-1].id;
    }
    return postsList;
  } catch (error) {
    console.error(`Failed to fetch forum posts : ${error}`);
    throw new Error(`Failed to fetch forum posts : ${error}`);
  }
}

async function fetchBatchOfPosts(session: Session, searchQuery: string | undefined, limit: number, lastId: number | undefined) : Promise<Array<GetPostsResponse>> {
  try {
    const path = new URL(`${process.env.NEXT_PUBLIC_API_BASE_URL}/posts`);
    path.searchParams.append("limit", `${limit}`);
    if (searchQuery) {
      path.searchParams.append("keyword", searchQuery);
    }
    if (lastId) {
      path.searchParams.append("lastId", `${lastId}`);
    }

    const postsList = await fetch(path, {
      headers: {
        'Authorization': getBearerToken(session),
      }
    });
    if (postsList.status != 200) {
      throw new Error(`Get batch of posts return status code ${postsList.status} : ${postsList.json()}`);
    }
    return postsList.json();
  } catch (error) {
    console.error(`Failed to fetch posts batch [limit=${limit}, lastId=${lastId}] : ${error}`);
    throw new Error(`Failed to fetch posts batch [limit=${limit}, lastId=${lastId}]: ${error}`);
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