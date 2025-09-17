import { BrowserRouter, Route, Routes } from "react-router-dom"
import Home from "./pages/Home"
import Rooms from "./pages/Rooms"
import NotFound from "./pages/error/NotFound"
import Register from "./pages/auth/Register"
import Login from "./pages/auth/Login"
import { ThemeProvider } from "./ui"
import { Playground } from "./pages/Playground"
import Layout from "./components/Layout"

export default function App() {
   return (
      <BrowserRouter>
         <ThemeProvider>
            <Layout>
               <Routes>
                  <Route path="/" element={<Home />} />
                  <Route path="/rooms" element={<Rooms />} />
                  <Route path="/register" element={<Register />} />
                  <Route path="/login" element={<Login />} />
                  <Route path="/playground" element={<Playground />} />
                  <Route path="*" element={<NotFound />} />
               </Routes>
            </Layout>
         </ThemeProvider>
      </BrowserRouter>
   )
}
