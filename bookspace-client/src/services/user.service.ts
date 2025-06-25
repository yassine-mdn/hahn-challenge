import type { UserDTO } from "@/types/user-dto";
import type { PageWrapper } from "@/types/page-wrapper";
import instance from "./axios";



export const updateUsername = async (curentName: string,newName:string): Promise<UserDTO> => {
  const response = await instance.put(`/api/v1/users/${curentName}`, {
    username: newName,
  } );
  return response.data;
};

export const fetchUserDetails = async (id: number): Promise<UserDTO> =>
    (await instance.get(`/api/v1/users/${id}`)).data;

export const fetchUserByUsername = async (username: string): Promise<UserDTO> =>
    (await instance.get(`/api/v1/users/${username}`)).data;

export const fetchAllUsers = async (
    pageNumber: number = 0,
    pageSize: number = 10
): Promise<PageWrapper<UserDTO>> =>
    (await instance.get(`/api/v1/users`, {
      params: {
        page: pageNumber,
        size: pageSize,
      },
    })).data;

export const createUser = async (userData: UserDTO): Promise<UserDTO> =>
    (await instance.post(`/api/v1/users`, userData)).data;

export const updateUser = async (id: number, userData: UserDTO): Promise<UserDTO> =>
    (await instance.put(`/api/v1/users/${id}`, userData)).data;

export const deleteUser = async (username: string): Promise<void> =>
    (await instance.delete(`/api/v1/users/${username}`)).data;