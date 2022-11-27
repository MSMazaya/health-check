import { create, verify } from "https://deno.land/x/djwt@v2.8/mod.ts";
import { ObjectId } from "https://deno.land/x/web_bson@v0.2.5/mod.ts";

const jwtKey = await crypto.subtle.generateKey(
  { name: "HMAC", hash: "SHA-512" },
  true,
  ["sign", "verify"],
);

export const getAuthKey = async (userId: ObjectId): Promise<string> => {
  const userStringId = userId.toHexString();
  const jwt = await create({ alg: "HS512", typ: "JWT" }, {
    userId: userStringId,
  }, jwtKey);

  return jwt;
};

export const authenticate = async (
  jwt: string,
): Promise<[boolean | null, { userId: ObjectId } | null]> => {
  try {
    const payload = await verify(jwt, jwtKey);

    return [true, {
      userId: ObjectId.createFromHexString(payload.userId as string),
    }];
  } catch {
    return [false, null];
  }
};
