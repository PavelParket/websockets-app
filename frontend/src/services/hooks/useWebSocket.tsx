import { useCallback, useEffect, useRef, useState } from "react";
import { WSClient } from "../wsClient";

interface UseWebSocketOptions<TIn, TOut> {
   roomId: string;
   token: string;
   onMessage: (data: TIn) => void;
}

export function useWebSocket<TIn = unknown, TOut = unknown>({ roomId, token, onMessage }: UseWebSocketOptions<TIn, TOut>) {
   const clientRef = useRef<WSClient<TIn, TOut> | null>(null);
   const [messages, setMessages] = useState<TIn[]>([]);
   const [connected, setConnected] = useState(false);

   useEffect(() => {
      const client = new WSClient<TIn, TOut>(roomId, token);
      clientRef.current = client;

      client.connect()
         .then(() => setConnected(true))
         .catch((e) => {
            console.error("WS connect failed:", e);
            setConnected(false);
         });

      client.subscribe((data: TIn) => {
         setMessages((prev) => [...prev, data]);
         onMessage?.(data);
      });

      return () => {
         console.log("🔌 cleanup: disconnect");
         client.disconnect();
         clientRef.current = null;
         setConnected(false);
      };
   }, [roomId, token, onMessage]);

   const sendMessage = useCallback((message: TOut) => {
      clientRef.current?.send(message);
   }, []);

   return { messages, sendMessage, connected };
}
