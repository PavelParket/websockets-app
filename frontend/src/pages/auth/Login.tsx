import { useState } from "react";
import { Box, Button, Card, Container, Form, FormField, Typography } from "../../ui";
import { Link } from "react-router-dom";

export default function Login() {
   const [form, setForm] = useState({ email: "", password: "" });

   const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      setForm({ ...form, [e.target.name]: e.target.value });
   };

   const handleSubmit = (e: React.FormEvent) => {
      e.preventDefault();
      console.log("Login form submitted:", form);
   };

   return (
      <Box style={{ padding: "56px 0" }}>
         <Container>
            <Box style={{ display: "grid", placeItems: "center" }}>
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
                        endAdornmentSrc="/images/at_fill.svg"
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
                        endAdornmentSrc="/images/safe_lock_fill.svg"
                        endAdornmentAlt="lock"
                     />
                     <Button type="submit" variant="solid">Sign In</Button>
                  </Form>
                  <Typography variant="caption" style={{ marginTop: 20, display: "block" }}>
                     Don't have an account?
                     <Link to="/register" className="link" style={{ marginLeft: 5 }}>
                        Sign Up
                     </Link>
                  </Typography>
               </Card>
            </Box>
         </Container>
      </Box>
   );
}
