import { BrowserRouter, Route, Routes } from "react-router-dom"
import Home from "./pages/Home"
import Rooms from "./pages/Rooms"
import NotFound from "./pages/NotFound"
import Register from "./pages/Register"
import { ThemeProvider } from "./ui"
import { Playground } from "./pages/Playground"

export default function App() {
   return (
      <BrowserRouter>
         <ThemeProvider>
            <Routes>
               <Route path="/" element={<Home />} />
               {/* <Route path="/rooms" element={<Rooms />} />
            <Route path="/register" element={<Register />} />
            <Route path="*" element={<NotFound />} /> */}
               <Route path="/playground" element={<Playground />} />
            </Routes>
         </ThemeProvider>
      </BrowserRouter>
   )
}