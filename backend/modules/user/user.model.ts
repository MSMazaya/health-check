import { ObjectId } from "https://deno.land/x/web_bson@v0.2.5/mod.ts";

export interface UserModel {
  email: string;
  password: string;
}

export type UserSchema = UserModel & {
  _id: ObjectId;
};
