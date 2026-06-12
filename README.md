# Zelacity - Zeladoria Urbana (Reporte Cidadão) 🌳🏢

Zelacity é um aplicativo Android desenvolvido para permitir que cidadãos registrem e acompanhem problemas de infraestrutura urbana (vazamentos, buracos, falta de iluminação, etc.) de forma rápida e eficiente.

## 🚀 Funcionalidades

*   **Autenticação Google:** Login seguro utilizando Google Sign-In integrado ao Firebase Auth.
*   **Geolocalização Automática:** Captura das coordenadas GPS (Latitude e Longitude) no momento do reporte.
*   **Entrada Manual de Endereço:** Caso o GPS falhe ou a permissão seja negada, o usuário pode digitar o endereço manualmente.
*   **Persistência Local (Room):** As denúncias são salvas localmente no dispositivo, permitindo o uso em locais sem sinal de internet.
*   **Sincronização em Nuvem (Firebase Realtime Database):** Uso de **WorkManager** para detectar conexão e sincronizar automaticamente as denúncias pendentes com a nuvem assim que a internet é restabelecida.
*   **Interface Intuitiva:** Lista de denúncias com status visual (Pendente/Sincronizado) e cores adaptadas para um tema "verde reciclável".
*   **Suporte a Dark Theme:** Interface totalmente adaptada para o Modo Escuro do Android com foco em acessibilidade.

## 🛠️ Tecnologias Utilizadas

*   **Linguagem:** Java
*   **Arquitetura:** MVVM (Model-View-ViewModel) + Repository Pattern
*   **Banco de Dados Local:** [Room Persistence Library](https://developer.android.com/training/data-storage/room)
*   **Banco de Dados Remoto:** [Firebase Realtime Database](https://firebase.google.com/docs/database)
*   **Autenticação:** [Firebase Auth](https://firebase.google.com/docs/auth) & Google Sign-In
*   **Geolocalização:** [Google Play Services Location](https://developers.google.com/android/guides/setup)
*   **Tarefas em Segundo Plano:** [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
*   **UI/UX:** Material Design 3

## 📋 Pré-requisitos para Rodar o Projeto

Para compilar e rodar o Zelacity, você precisará configurar o Firebase no seu ambiente:

1.  Crie um projeto no [Firebase Console](https://console.firebase.google.com/).
2.  Adicione um aplicativo Android com o pacote `com.example.zelacity`.
3.  Vincule o **SHA-1** da sua chave de desenvolvimento (`signingReport`) nas configurações do projeto no Firebase.
4.  Baixe o arquivo `google-services.json` e coloque-o na pasta `app/` do projeto.
5.  Ative o **Google Sign-In** na aba de Autenticação.
6.  Ative o **Realtime Database** e configure as regras de leitura/escrita para `auth != null`.

---
Desenvolvido como projeto prático de desenvolvimento Android, aplicando conceitos de persistência, conexão remota, serviços de localização e boas práticas de arquitetura.
