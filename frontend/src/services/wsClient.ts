export class WSClient<TIn = unknown, TOut = unknown> {
   private socket: WebSocket | null = null;
   private roomId: string;
   private token: string;
   private onMessage?: (data: TIn) => void;

   constructor(roomId: string, token: string) {
      this.roomId = roomId;
      this.token = token;
   }

   connect() {
      return new Promise<void>((resolve, reject) => {
         this.socket = new WebSocket(`ws://localhost:8080/ws/game?roomId=${encodeURIComponent(this.roomId)}&token=${encodeURIComponent(this.token)}`);

         this.socket.onopen = () => {
            console.log("✅ WebSocket connected");
            resolve();
         };

         this.socket.onerror = (err) => {
            console.error("❌ WebSocket error:", err);
            reject(err);
         };

         this.socket.onmessage = (event: MessageEvent) => {
            try {
               const data = JSON.parse(event.data) as TIn;
               this.onMessage?.(data);
            } catch {
               console.warn("Raw WS message (not JSON):", event.data);
            }
         };

         this.socket.onclose = (e) => {
            console.log("🔌 WebSocket closed", e);
         };
      });
   }

   send(message: TOut): void {
      if (this.socket?.readyState === WebSocket.OPEN) {
         this.socket.send(JSON.stringify(message));
      } else {
         console.warn("⚠️ WebSocket is not open. Cannot send:", message);
      }
   }

   subscribe(callback: (data: TIn) => void): void {
      this.onMessage = callback;
   }

   disconnect(): void {
      if (this.socket) {
         this.socket.close();
         this.socket = null;
      }
   }
}
