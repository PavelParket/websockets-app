import { Box, Button, Card, Container, Typography } from "../../ui";
import { Link } from "react-router-dom";

export default function NotFound() {
   return (
      <Box style={{ padding: "56px 0" }}>
         <Container>
            <Box style={{ display: "grid", placeItems: "center" }}>
               <Card style={{ width: "min(420px, 100%)", textAlign: "center" }}>
                  <Typography variant="h2">404 — Page Not Found</Typography>
                  <Typography variant="body">The page you are looking for does not exist.</Typography>
                  <Link to="/" style={{ textDecoration: "none" }}>
                     <Button variant="solid" style={{ marginTop: "16px" }}>
                        Back to Home
                     </Button>
                  </Link>
               </Card>
            </Box>
         </Container>
      </Box>
   );
}
