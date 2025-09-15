import { BrowserRouter, Route, Routes } from "react-router-dom"
import Home from "./pages/Home"
import Rooms from "./pages/Rooms"
import NotFound from "./pages/NotFound"
import Register from "./pages/Register"

export default function App() {
   return (
      <BrowserRouter>
         <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/rooms" element={<Rooms />} />
            <Route path="/register" element={<Register />} />
            <Route path="*" element={<NotFound />} />
         </Routes>
      </BrowserRouter>
   )
}