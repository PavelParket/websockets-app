import { useEffect, useState } from "react";
import { Box, Button, Card, Container, Form, FormField, Typography } from "../../ui";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import type { AppDispatch, RootState } from "../../store/store";
import { login } from "../../store/feature/authSlice";
import { useThemedIcon } from "../../ui/hooks/useThemedIcon";

export default function Login() {
   const [form, setForm] = useState({ email: "", password: "" });
   const dispatch = useDispatch<AppDispatch>();
   const { id, loading, error } = useSelector((state: RootState) => state.auth);
   const { getIcon } = useThemedIcon();

   const navigate = useNavigate();
   const location = useLocation();
   const from = (location.state as { from?: Location })?.from?.pathname || "/";

   useEffect(() => {
      if (id) {
         navigate(from, { replace: true });
      }
   }, [id, navigate, from]);

   const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      setForm({ ...form, [e.target.name]: e.target.value });
   };

   const handleSubmit = (e: React.FormEvent) => {
      e.preventDefault();
      dispatch(login(form));
   };

   return (
      <Box style={{ padding: "56px 0" }}>
         <Container>
            <Box style={{ display: "grid", placeItems: "center", minWidth: "500px" }}>
               <Card style={{ width: "min(420px, 100%)", textAlign: "center", padding: "30px 40px" }}>
                  <Typography variant="h2">Sign In</Typography>
                  <Form onSubmit={handleSubmit} gap="16px" style={{ marginTop: 16 }}>
                     <FormField
                        placeholder="Email"
                        type="email"
                        name="email"
                        value={form.email}
                        onChange={handleChange}
                        required
                        rounded
                        endAdornmentSrc={getIcon("apersant")}
                        endAdornmentAlt="email"
                     />
                     <FormField
                        placeholder="Password"
                        type="password"
                        name="password"
                        value={form.password}
                        onChange={handleChange}
                        required
                        rounded
                        endAdornmentSrc={getIcon("safeLock")}
                        endAdornmentAlt="lock"
                     />
                     <Button type="submit" variant="solid" disabled={loading}>
                        {loading ? "Loading..." : "Sign In"}
                     </Button>
                  </Form>
                  <Typography variant="caption" style={{ marginTop: 20, display: "block" }}>
                     Don't have an account?
                     <Link to="/register" className="link" style={{ marginLeft: 5 }}>
                        Sign Up
                     </Link>
                  </Typography>
                  <Typography variant="caption" style={{ color: "red", marginTop: 10, display: "block" }}>
                     {error}
                  </Typography>
               </Card>
            </Box>
         </Container>
      </Box>
   );
}
