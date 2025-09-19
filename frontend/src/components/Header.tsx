import { Link, useNavigate } from "react-router-dom"
import { Button, Navbar, ThemeSwitcher, Typography } from "../ui"
import { useDispatch, useSelector } from "react-redux";
import type { RootState } from "../store/store";
import { logout } from "../store/feature/authSlice";

export default function Header() {
   const { id } = useSelector((state: RootState) => state.auth);
   const dispatch = useDispatch();
   const navigate = useNavigate();

   const handleLogout = () => {
      dispatch(logout());
      setTimeout(() => {
         navigate("/");
      }, 0);
   };

   return (
      <Navbar
         brand={(
            <Link to="/" style={{ textDecoration: "none" }}>
               <Typography variant="h3">WebSockets App</Typography>
            </Link>
         )}
         right={(
            <>
               {!id && (
                  <>
                     <Link to="/register">
                        <Button variant="solid">Sign Up</Button>
                     </Link>
                     <Link to="/login">
                        <Button variant="solid">Sign In</Button>
                     </Link>
                  </>
               )}

               {id && (
                  <Button variant="ghost" onClick={handleLogout}>
                     Logout
                  </Button>
               )}

               <ThemeSwitcher />
            </>
         )}
      />
   );
}
