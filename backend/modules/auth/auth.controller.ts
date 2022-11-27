import { Context } from "https://deno.land/x/oak@v11.1.0/context.ts";
import { UserModel } from "../user/user.model.ts";
import { createUser, getUserByEmail } from "../user/user.repository.ts";
import { hashPassword, validateHash } from "../../utils/hash.ts";
import { getAuthKey } from "../../utils/jwt.ts";

const login = async (
  context: Context<Record<string, any>, Record<string, any>>,
) => {
  const body = context.request.body();
  const { email, password }: UserModel = await body.value;

  if (email && password) {
    const existingUserData = await getUserByEmail(email);

    if (existingUserData) {
      const isValid = validateHash(password, existingUserData.password);
      if (isValid) {
        const authKey = await getAuthKey(existingUserData._id);
        context.response.body = {
          status: 200,
          message: "Successfully login",
          payload: {
            authKey,
          },
        };
        return;
      } else {
        context.response.body = {
          status: 400,
          message: "Invalid email or password",
        };
        return;
      }
    }

    const hashedPassword = hashPassword(password);

    const user: UserModel = {
      email,
      password: hashedPassword,
    };

    const id = await createUser(user);

    const jwt = await getAuthKey(id);

    context.response.body = {
      status: 200,
      message: "User created",
      payload: {
        token: jwt,
      },
    };

    return;
  }

  context.response.body = {
    status: 400,
    message: "Bad Payload",
  };
};

export default { login };
