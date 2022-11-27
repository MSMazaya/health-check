import db from "../../utils/db.ts";
import { UserModel, UserSchema } from "./user.model.ts";

export const createUser = async (user: UserModel) => {
  const userCollection = db.collection<UserSchema>("users");

  return await userCollection.insertOne(user);
};

export const getUserByEmail = async (email: string) => {
  const userCollection = db.collection<UserSchema>("users");

  return await userCollection.findOne({
    email,
  });
};
