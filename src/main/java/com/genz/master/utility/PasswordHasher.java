package com.genz.master.utility;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    /**
     * Hash sebuah password menggunakan BCrypt.
     *
     * @param password Password yang akan di-hash.
     * @return String hasil hashing.
     */
    public static String hash(String password) {
        // Generate salt dan hash password
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Verifikasi apakah password cocok dengan hash yang tersimpan.
     *
     * @param password Password yang akan diverifikasi.
     * @param hashedPassword Hash yang tersimpan di database.
     * @return true jika password cocok, false jika tidak.
     */
    public static boolean verify(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}