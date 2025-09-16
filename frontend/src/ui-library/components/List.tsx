type ListProps = {
   items: React.ReactNode[];
   ordered?: boolean;
};

export function List({ items, ordered }: ListProps) {
   const Tag = ordered ? "ol" : "ul";
   return (
      <Tag style={{ paddingLeft: "1.2rem", color: "var(--color-text)" }}>
         {items.map((item, i) => (
            <li key={i}>{item}</li>
         ))}
      </Tag>
   );
}