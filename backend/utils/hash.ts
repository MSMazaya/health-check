import { Md5 } from "https://deno.land/std@0.120.0/hash/md5.ts";

export const hashPassword = (password: string) => {
  // hash with md5
  const md5 = new Md5();
  const hashedPassword = md5.update(password).toString();

  return hashedPassword;
};
