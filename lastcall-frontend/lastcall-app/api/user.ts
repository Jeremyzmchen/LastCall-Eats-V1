import { apiClient } from './client';

export interface UserProfileResponse {
  id: number;
  email: string;
  nickname: string;
  avatarUrl: string;
}

export interface FavoriteMerchantResponse {
  merchantId: number;
  merchantName: string;
  merchantAddress: string;
  favoritedAt: string;
}

export const getUserProfile = () =>
  apiClient.get<{ code: number; data: UserProfileResponse }>('/api/user/profile');

export const updateUserProfile = (nickname: string) =>
  apiClient.put<{ code: number; data: UserProfileResponse }>('/api/user/profile', { nickname });

export const getFavorites = () =>
  apiClient.get<{ code: number; data: FavoriteMerchantResponse[] }>('/api/user/favorites');

export const addFavorite = (merchantId: number) =>
  apiClient.post(`/api/user/favorites/${merchantId}`);

export const removeFavorite = (merchantId: number) =>
  apiClient.delete(`/api/user/favorites/${merchantId}`);

export const checkFavorite = (merchantId: number) =>
  apiClient.get<{ code: number; data: boolean }>(`/api/user/favorites/${merchantId}`);
