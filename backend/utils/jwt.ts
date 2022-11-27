import { create, verify } from "https://deno.land/x/djwt@v2.8/mod.ts";

const jwtKey = await crypto.subtle.generateKey(
  { name: "HMAC", hash: "SHA-512" },
  true,
  ["sign", "verify"],
);

export const getAuthKey = async (email: string): Promise<string> => {
  const jwt = await create({ alg: "HS512", typ: "JWT" }, { email }, jwtKey);

  return jwt;
};

export const authenticate = async (
  jwt: string,
): Promise<[boolean | null, { email: string } | null]> => {
  try {
    const payload = await verify(jwt, jwtKey);
    return [true, { email: payload.email as string }];
  } catch {
    return [false, null];
  }
};
