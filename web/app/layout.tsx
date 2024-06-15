"use client"

import { Inter } from "next/font/google";
import "./globals.css";
import Link from "next/link";
import { SessionProvider } from "next-auth/react";

const inter = Inter({ subsets: ["latin"] });

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <div className="flex flex-col h-full w-full">
          <nav className="flex items-center justify-between flex-wrap bg-blue-800 p-6">
            <div className="flex items-center flex-shrink-0 text-white mr-6">
              <span className="font-semibold text-xl">Simple Forum</span>
            </div>
            <div className="w-full block flex-grow lg:flex lg:items-center lg:w-auto">
              <div className="text-sm">
                <Link href="/" className="block mt-4 lg:inline-block lg:mt-0 text-gray-300 hover:text-white mr-4">
                  Home
                </Link>
                <Link href="/forums" className="block mt-4 lg:inline-block lg:mt-0 text-gray-300 hover:text-white mr-4">
                  Forums
                </Link>
                <Link href="/forums/ssr" className="block mt-4 lg:inline-block lg:mt-0 text-gray-300 hover:text-white mr-4">
                  Forums (SSR)
                </Link>
              </div>
            </div>
          </nav>
          <main className="h-full w-full">
            <SessionProvider>
              {children}
            </SessionProvider>
          </main>
        </div>
      </body>
    </html>
  );
}
