export interface UsuarioResponse {
  username: string;
  roles: Rol[];
  enabled: boolean;
  email: string;
}

export interface Rol {
  name: string;
  permissions: Permiso[];
}

export interface Permiso {
  name: string;
}
