import { Container, Img, List, Typography, Box, Button, useTheme, Card, Input, ThemeSwitcher } from "../ui";

export function Playground() {
   const { theme, toggleTheme } = useTheme();

   return (
      <Container maxWidth="900px">
         <Box style={{ display: "flex", gap: "2rem", flexWrap: "wrap" }}>
            {/* Card + Typography + Inputs */}
            <Card style={{ flex: 1, minWidth: "300px" }}>
               <Typography variant="h1">Login</Typography>
               <Input placeholder="Username" name="username" />
               <Input placeholder="Email" type="email" name="email" />
               <Input placeholder="Password" type="password" name="password" />
               <Button style={{ width: "100%", marginTop: "1rem" }}>Login</Button>
               <Button variant="ghost" onClick={toggleTheme} style={{ marginTop: "0.5rem" }}>
                  Switch to {theme === "light" ? "Dark" : "Light"} Theme
               </Button>
            </Card>

            {/* Typography showcase */}
            <Card style={{ flex: 1, minWidth: "300px" }}>
               <Typography variant="h1">H1 Heading</Typography>
               <Typography variant="h2">H2 Heading</Typography>
               <Typography variant="h3">H3 Heading</Typography>
               <Typography variant="body">Body text example</Typography>
               <Typography variant="caption">Caption text example</Typography>
            </Card>

            {/* Buttons */}
            <Card style={{ flex: 1, minWidth: "300px" }}>
               <Button style={{ margin: "0.3rem" }}>Solid</Button>
               <Button variant="outline" style={{ margin: "0.3rem" }}>Outline</Button>
               <Button variant="ghost" style={{ margin: "0.3rem" }}>Ghost</Button>
            </Card>

            {/* List + Img */}
            <Card style={{ flex: 1, minWidth: "300px" }}>
               <Typography variant="h2">List Example</Typography>
               <List items={["Item 1", "Item 2", "Item 3"]} />
               <Typography variant="h2" style={{ marginTop: "1rem" }}>Image Example</Typography>
               <Img src="#" alt="image" rounded />
            </Card>

            <Card>
               <Typography variant="h2">Theme Switcher</Typography>
               <ThemeSwitcher />
            </Card>
         </Box>
      </Container>
   );
}