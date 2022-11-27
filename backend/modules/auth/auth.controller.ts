import { Context } from "https://deno.land/x/oak@v11.1.0/context.ts";
import { UserModel } from "../user/user.model.ts";
import { createUser } from "../user/user.repository.ts";
import { hashPassword } from "../../utils/hash.ts";
import { getAuthKey } from "../../utils/jwt.ts";

const login = async (
  context: Context<Record<string, any>, Record<string, any>>,
) => {
  const body = context.request.body();
  const { email, password }: UserModel = await body.value;

  if (email && password) {
    const hashedPassword = hashPassword(password);

    const user: UserModel = {
      email,
      password: hashedPassword,
    };

    await createUser(user);

    const jwt = await getAuthKey(user.email);

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
