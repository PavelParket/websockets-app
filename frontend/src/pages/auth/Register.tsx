import { useState } from "react";
import { Box, Button, Card, Container, Form, FormField, Typography, useThemedIcon } from "../../ui";
import { Link } from "react-router-dom";

export default function Register() {
   const [form, setForm] = useState({
      username: "",
      email: "",
      password: "",
   });

   const { getIcon } = useThemedIcon();

   const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      setForm({ ...form, [e.target.name]: e.target.value });
   };

   const handleSubmit = (e: React.FormEvent) => {
      e.preventDefault();
      console.log("Register form submitted:", form);
   };

   return (
      <Box style={{ padding: "56px 0" }}>
         <Container>
            <Box style={{ display: "grid", placeItems: "center" }}>
               <Card style={{ width: "min(420px, 100%)", textAlign: "center", padding: "30px 40px" }}>
                  <Typography variant="h2">Sign Up</Typography>
                  <Form onSubmit={handleSubmit} gap="16px" style={{ marginTop: 16 }}>
                     <FormField
                        placeholder="Username"
                        name="username"
                        value={form.username}
                        onChange={handleChange}
                        required
                        rounded
                        endAdornmentSrc={getIcon("user")}
                        endAdornmentAlt="user"
                     />
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
                     <Button type="submit" variant="solid">Sign Up</Button>
                  </Form>
                  <Typography variant="caption" style={{ marginTop: 20, display: "block" }}>
                     Already have an account?
                     <Link to="/login" className="link" style={{ marginLeft: 5 }}>
                        Sign In
                     </Link>
                  </Typography>
               </Card>
            </Box>
         </Container>
      </Box>
   );
}
