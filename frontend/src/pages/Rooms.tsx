import { Layout, Navbar } from "../ui";

export default function Rooms() {
   return (
      <Layout
         navbar={<Navbar brand="Websockets App" />}
      >
         <h2>Rooms</h2>
         <p>Here will be the list of user rooms.</p>
      </Layout>
   );
}