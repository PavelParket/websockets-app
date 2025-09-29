import api from "../api/mediatorServiceApi";

export async function getUsersInRoom(roomId: string): Promise<string[]> {
   try {
      const response = await api.get<string[]>("/ws/users", {
         params: { roomId },
      });

      return response.data;
   } catch (e) {
      console.error("Error getting users", e);
      throw e;
   }
}