import { Link } from "react-router-dom";
import { Box, Button, Card, Container, Grid, List, Stack, Typography } from "../ui"

export default function Home() {
   return (
      <Box style={{ padding: "56px 0" }}>
         <Container>
            <Grid columns="repeat(auto-fit, minmax(280px, 1fr))" align="center" gap="24px">
               <Stack gap="12px">
                  <Typography variant="h1">Realtime Chats</Typography>
                  <Typography variant="body">Минималистичный UI с двумя темами и стеклянными поверхностями.</Typography>
                  <Stack direction="row" gap="12px">
                     <Button variant="solid">Начать</Button>
                     <Link to="/rooms">
                        <Button variant="outline">Rooms</Button>
                     </Link>

                     {/* STATIC COMPONENTS FOR CREATING DESIGN */}
                     <Link to="/static">
                        <Button variant="ghost">Design</Button>
                     </Link>

                     <Link to="/playground">
                        <Button variant="outline">Playground</Button>
                     </Link>
                  </Stack>
               </Stack>
               <Card>
                  <Stack gap="8px">
                     <Typography variant="h3">Особенности</Typography>
                     <List gap="4px" items={[
                        "Тёмная/светлая темы",
                        "Монохромная палитра",
                        "Прозрачные компоненты",
                        "Универсальные примитивы",
                     ]} />
                  </Stack>
               </Card>
            </Grid>
         </Container>
      </Box>
   );
}
