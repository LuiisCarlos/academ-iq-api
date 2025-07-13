package dev.luiiscarlos.academ_iq_api.shared.security;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class RSAKeyProperties {

    private String[] possiblePaths;

    private RSAPublicKey publicKey;

    private RSAPrivateKey privateKey;

    public RSAKeyProperties(Environment env) {
        this.possiblePaths = loadPaths(env);
        this.privateKey = loadPrivateKey();
        this.publicKey = loadPublicKey();
    }

    private RSAPrivateKey loadPrivateKey() {
        InputStream is = getInputStream("private-key.pem");

        if (Objects.isNull(is))
            throw new RuntimeException("Private key not found in any known path");

        try (is) {
            String key = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replaceAll("-----\\w+ PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return (RSAPrivateKey) kf.generatePrivate(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException("Failed to load public key", ex);
        }
    }

    private RSAPublicKey loadPublicKey() {
        InputStream is = getInputStream("public-key.pem");

        if (Objects.isNull(is))
            throw new RuntimeException("Public key not found in any known path");

        try (is) {
            String key = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replaceAll("-----\\w+ PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return (RSAPublicKey) kf.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException("Failed to load public key", ex);
        }
    }

    private String[] loadPaths(Environment env) {
        String pathsStr = env.getProperty("rsa.key.paths");

        System.out.println("\n\nRSA key paths:" + pathsStr + "\n\n");

        if (Objects.isNull(pathsStr) || pathsStr.isEmpty())
            throw new RuntimeException("Failed to load possibles paths for RSA keys");

        return pathsStr.split(",");
    }

    private InputStream getInputStream(String filename) {
        try {
            for (String pathStr : possiblePaths) {
                Path path = Paths.get(pathStr + filename);
                if (Files.exists(path))
                    return Files.newInputStream(path);
            }
        } catch (IOException ignored) { }

        return null;
    }

}
