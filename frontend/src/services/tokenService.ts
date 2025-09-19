let accessToken: string | null = null;
let refreshToken: string | null = null;

export const tokenService = {
   setTokens: (tokens: { accessToken: string, refreshToken: string }) => {
      accessToken = tokens.accessToken;
      refreshToken = tokens.refreshToken;
   },
   clear: () => {
      accessToken = null;
      refreshToken = null;
   },
   getAccessToken: () => accessToken,
   getRefreshToken: () => refreshToken,
};