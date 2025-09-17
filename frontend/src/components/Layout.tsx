import type { ReactNode } from "react"
import Header from "./Header"
import Footer from "./Footer"
import { Box } from "../ui"

export default function Layout({ children }: { children: ReactNode }) {
   return (
      <Box style={{ minHeight: "100dvh", display: "flex", flexDirection: "column" }}>
         <Header />
         <Box component="main" style={{ flex: 1 }}>
            {children}
         </Box>
         <Footer />
      </Box>
   )
}
