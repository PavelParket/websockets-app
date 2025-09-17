import { Link } from "react-router-dom"
import { Button, Navbar, ThemeSwitcher, Typography } from "../ui"

export default function Header() {
   return (
      <Navbar
         brand={(
            <Link to="/" style={{ textDecoration: "none" }}>
               <Typography variant="h3">WebSockets App</Typography>
            </Link>
         )}
         right={(
            <>
               <Link to="/register">
                  <Button variant="solid">Sign Up</Button>
               </Link>
               <Link to="/login">
                  <Button variant="solid">Sign In</Button>
               </Link>
               <ThemeSwitcher />
            </>
         )}
      />
   )
}
