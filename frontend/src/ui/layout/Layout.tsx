import "./Layout.css";

export interface LayoutProps {
   navbar?: React.ReactNode;
   children: React.ReactNode;
}

export default function Layout({ navbar, children }: LayoutProps) {
   return (
      <div className="ui-layout">
         {navbar && <header className="ui-layout__navbar">{navbar}</header>}
         <main className="ui-layout__content">{children}</main>
      </div>
   );
}