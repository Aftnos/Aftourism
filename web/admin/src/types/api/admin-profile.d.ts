declare namespace Api {
  namespace AdminProfile {
    interface Profile {
      id: number
      username: string
      realName?: string
      phone?: string
      email?: string
      avatar?: string
      introduction?: string
    }

    interface UpdateParams {
      realName?: string
      phone?: string
      email?: string
      avatar?: string
      introduction?: string
    }

    interface PasswordUpdateParams {
      currentPassword: string
      newPassword: string
      confirmPassword: string
    }
  }
}
