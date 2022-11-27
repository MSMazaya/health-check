import db from "../../utils/db.ts";
import { UserModel, UserSchema } from "./user.model.ts";

export const createUser = async (user: UserModel) => {
  const userCollection = db.collection<UserSchema>("users");

  await userCollection.insertOne(user);
};
