export default function PostCard({
  title, content, createdAt, updatedAt
} : {
  title: string,
  content: string,
  createdAt: string,
  updatedAt: string
}) {
  return (
    <div className="rounded-md p-4 mb-4 z-10 shadow">
      <p className="font-semibold text-xl">{title}</p>
      <p className="flex-wrap text-base mb-2">{content}</p>
      <p className="text-xs italic text-gray-500">Created at: {new Date(Date.parse(createdAt)).toLocaleString()}</p>
      <p className="text-xs italic text-gray-500">Last updated: {new Date(Date.parse(updatedAt)).toLocaleString()}</p>
    </div>
  )
}