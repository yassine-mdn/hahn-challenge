import api from "./axios";
import type {UserDTO} from "@/types/user-dto.ts";



export const updateUsername = async (curentName: string,newName:string): Promise<UserDTO> => {
  const response = await api.put(`/api/v1/users/${curentName}`, {
    username: newName,
  } );
  return response.data;
}; 