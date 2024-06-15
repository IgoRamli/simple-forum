'use client'

export default function Button({ className, text, onClick } : { className?: string, text: string, onClick?: () => void }) {
  return <button className={`${className} bg-blue-500 hover:bg-blue-700 items-center text-white font-bold py-2 px-4 rounded`} onClick={onClick}>
    {text}
  </button>
}