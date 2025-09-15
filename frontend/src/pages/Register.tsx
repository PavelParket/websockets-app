import { useState } from "react";
import { Box, Button, Card, Form, FormField, Layout, Navbar } from "../ui";

export default function Register() {
   const [form, setForm] = useState({
      username: "",
      email: "",
      password: "",
   });

   const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      setForm({ ...form, [e.target.name]: e.target.value });
   };

   const handleSubmit = (e: React.FormEvent) => {
      e.preventDefault();
      console.log("Register form submitted:", form);
   };

   return (
      <Layout navbar={<Navbar brand="Websockets App" />}>
         <Box display="flex" justifyContent="center" margin="5rem 0 0 0">
            <Card padding="lg" style={{ width: "400px", textAlign: "center" }}>
               <h2>Sign Up</h2>
               <Form onSubmit={handleSubmit} gap="16px">
                  <FormField
                     placeholder="Username"
                     name="username"
                     value={form.username}
                     onChange={handleChange}
                     required
                  />
                  <FormField
                     placeholder="Email"
                     type="email"
                     name="email"
                     value={form.email}
                     onChange={handleChange}
                     required
                  />
                  <FormField
                     placeholder="Password"
                     type="password"
                     name="password"
                     value={form.password}
                     onChange={handleChange}
                     required
                  />
                  <Button type="submit" variant="primary" size="md">
                     Register
                  </Button>
               </Form>
            </Card>
         </Box>
      </Layout>
   );
}