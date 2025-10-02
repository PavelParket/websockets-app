import { BrowserRouter, Route, Routes } from "react-router-dom"
import Home from "./pages/Home"
import Rooms from "./pages/rooms/Rooms"
import NotFound from "./pages/error/NotFound"
import Register from "./pages/auth/Register"
import Login from "./pages/auth/Login"
import { ThemeProvider } from "./ui"
import Playground from "./pages/Playground"
import Layout from "./components/Layout"
import { ProtectedRoute } from "./router/ProtectedRoute"
import Forbidden from "./pages/error/Forbidden"
import ChatRoom from "./pages/rooms/ChatRoom"
import TicTacToeRoom from "./pages/rooms/TicTacToeRoom"

export default function App() {
   return (
      <BrowserRouter>
         <ThemeProvider>
            <Routes>
               <Route element={<Layout />}>
                  <Route path="/" element={<Home />} />

                  <Route element={<ProtectedRoute />}>
                     <Route path="/rooms" element={<Rooms />} />
                     <Route path="/room/chat/:roomId" element={<ChatRoom />} />
                     <Route path="/room/game/:roomId" element={<TicTacToeRoom />} />
                  </Route>

                  <Route element={<ProtectedRoute roles={["ADMIN"]} />}>
                     <Route path="/playground" element={<Playground />} />
                  </Route>
               </Route>

               <Route element={<Layout centered />}>
                  <Route path="/register" element={<Register />} />
                  <Route path="/login" element={<Login />} />

                  <Route path="/forbidden" element={<Forbidden />} />
                  <Route path="*" element={<NotFound />} />
               </Route>
            </Routes>
         </ThemeProvider>
      </BrowserRouter >
   )
}
