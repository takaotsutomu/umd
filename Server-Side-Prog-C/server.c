#include <stdio.h>
#include <string.h>	/* for using strtok, strcmp, etc */
#include <unistd.h>
#include <sys/types.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>

#include "common.h"

#define PRT_SIZE 6
#define FLD_SIZE 85
#define MAX_FLD 4
#define ABCD_SIZE 4

void handle_client(char *serv_name, char *clnt_name, char *port, int clnt_socket);
int hello(char *message);
int bye(char *message);
    
int main (int argc, char **argv){
    struct addrinfo *addr_list, addr_criteria;
    struct sockaddr_in serv_addr, clnt_addr;
    in_port_t serv_port;
    socklen_t clnt_addr_len;
    char serv_name[INET_ADDRSTRLEN];
    char clnt_name[INET_ADDRSTRLEN];
    char serv_port_str[PRT_SIZE];
    int serv_sock, clnt_sock;
    int rtn_val;
    void *bi_addr;
    
    if (argc > 1) {
        strcpy(serv_port_str, argv[1]);
    } else {
        sprintf(serv_port_str, "%d", SERVER_PORT);
    }
    
    /* translate hostname to ip address */
    memset(&addr_criteria, 0, sizeof(addr_criteria));
    addr_criteria.ai_family = AF_INET;
    addr_criteria.ai_socktype = SOCK_STREAM;
    addr_criteria.ai_protocol = IPPROTO_TCP;
    
    if ((rtn_val = getaddrinfo(SERVER_HOSTNAME, serv_port_str, &addr_criteria, &addr_list))) {
        fputs("getaddrinfo() failed\n", stderr);
        exit(1);
    }
    
    bi_addr = &((struct sockaddr_in *)addr_list->ai_addr)->sin_addr;
    
    if (inet_ntop(addr_list->ai_addr->sa_family, bi_addr, serv_name,
                  sizeof(serv_name)) == NULL) {
        fputs("invalid address\n", stderr);
        exit(1);
    }
    
    serv_port = ((struct sockaddr_in *)addr_list->ai_addr)->sin_port;
    
    freeaddrinfo(addr_list);
    
    
    /* create socket for incoming connections */
    if ((serv_sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0) {
        fputs("socket() failed\n", stderr);
        exit(1);
    }
    
    memset(&serv_addr, 0, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    serv_addr.sin_port = serv_port;
    
    if (bind(serv_sock, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0) {
        fputs("bind() failed\n", stderr);
        exit(1);
    }
    
    if (listen(serv_sock, MAXPENDING) < 0) {
        fputs("listen() failed\n", stderr);
    }
    
    while (1) {
        clnt_addr_len = sizeof(clnt_addr);
        
        if ((clnt_sock = accept(serv_sock, (struct sockaddr *)&clnt_addr, &clnt_addr_len)) < 0) {
            fputs("accept() failed\n", stderr);
            exit(1);
        }
        
        if ((inet_ntop(AF_INET, &clnt_addr.sin_addr.s_addr, clnt_name, sizeof(clnt_name))) != NULL) {
            //printf("handling client %s/%d\n", clnt_name, ntohs(clnt_addr.sin_port));
        } else {
            fputs("unable to get client address", stderr);
        }
        
        handle_client(serv_name, clnt_name, serv_port_str, clnt_sock);
    }
}

void handle_client(char *serv_name, char *clnt_name, char *port, int clnt_socket) {
    char message[MAX_STR_SIZE];
    char fields[MAX_FLD][FLD_SIZE];
    char login_id[FLD_SIZE], name[FLD_SIZE];
    char a_str[ABCD_SIZE], b_str[ABCD_SIZE];
    char c_str[ABCD_SIZE], d_str[ABCD_SIZE];
    int a, b, c, d, cookie = -1;
    int rtn, len, val, cok;
    char *token;
    int num;

    read(clnt_socket, message, MAX_STR_SIZE);

    if (hello(message)) {
        sscanf(message, "%s %s %s %s", fields[0], fields[1], fields[2], fields[3]);
    } else {
        printf("**Error** from %s:%s\n", clnt_name, port);
        fflush(stdout);
        close(clnt_socket);
        return;
    }
    
    memset(message, 0, MAX_STR_SIZE);
    
    strcpy(login_id, fields[2]);
    strcpy(name, fields[3]);
    
    sscanf(clnt_name, "%s.%s.%s.%s", a_str, b_str, c_str, d_str);
    a = atoi(a_str);
    b = atoi(b_str);
    c = atoi(c_str);
    d = atoi(d_str);
    cookie = ((a + b + c + d) * 13) % 1111;
    sprintf(message, "%s STATUS %d %s:%s\n", MAGIC_STRING, cookie, serv_name, port);
    len = strlen(message);
    write(clnt_socket, message, len);
    
    memset(message, 0, MAX_STR_SIZE);
    read(clnt_socket, message, MAX_STR_SIZE);
    
    if (bye(message)) {
        sscanf(message, "%s %s %d", fields[0], fields[1], &cok);
    } else {
        printf("**Error** from %s:%s\n", clnt_name, port);
        fflush(stdout);
        close(clnt_socket);
        return;
    }
    
    if (cok != cookie) {
        printf("**Error** from %s:%s\n", clnt_name, port);
        fflush(stdout);
        close(clnt_socket);
        return;
    }
    
    memset(message, 0, MAX_STR_SIZE);
    sprintf(message, "%s SERVER_BYE", MAGIC_STRING);
    len = strlen(message);
    write(clnt_socket, message, len);
    printf("%d %s %s from %s:%s\n", cookie, login_id, name, clnt_name, port);
    fflush(stdout);
    close(clnt_socket);
}

int hello(char *message) {
    char *magic = MAGIC_STRING;
    char *hello = "HELLO";
    int len1, len2;
    int num, i, j;
    
    len1 = strlen(magic);
    len2 = strlen(hello);
    
    i = 0;
    while (message[i] && message[i] == ' ') {
        i++;
        
        if (!(message[i]) || message[i] == '\n') {
            return 0;
        }
    }
    
    num = 0;
    j = 0;
    while (message[i] && message[i] == magic[j]) {
        num++;
        i++;
        j++;
    }
    
    if (num != len1 || !(message[i]) || message[i] == '\n') {
        return 0;
    }
    
    while (message[i] && message[i] == ' ') {
        i++;
        
        if (!(message[i]) || message[i] == '\n') {
            return 0;
        }
    }
    
    num = 0;
    j = 0;
    while (message[i] && message[i] == hello[j]) {
        num++;
        i++;
        j++;
    }
    
    if (num != len2 || !(message[i]) || message[i] == '\n') {
        return 0;
    }
    
    while (message[i] && message[i] == ' ') {
        i++;
        
        if (!(message[i]) || message[i] == '\n') {
            return 0;
        }
    }
    
    while (message[i] && message[i] != ' ') {
        i++;
        
        if (!(message[i]) || message[i] == '\n') {
            return 0;
        }
    }
    
    while (message[i] && message[i] == ' ') {
        i++;
        
        if (!(message[i]) || message[i] == '\n') {
            return 0;
        }
    }
    
    while (message[i] && message[i] != ' ') {
        i++;
        
        if (!(message[i]) || message[i] == '\n') {
            return 0;
        }
    }
    
    while (message[i] && message[i] == ' ') {
        i++;
    }
    
    if (!(message[i]) || message[i] == '\n') {
        return 1;
    }
    return 0;
}

int bye(char *message) {
    char *magic = MAGIC_STRING;
    char *bye = "CLIENT_BYE";
    int len1, len2;
    int num, i, j;
    
    len1 = strlen(magic);
    len2 = strlen(bye);
    
    i = 0;
    while (message[i] && message[i] == ' ') {
        i++;
        
        if (!(message[i]) || message[i] == '\n') {
            return 0;
        }
    }
    
    num = 0;
    j = 0;
    while (message[i] && message[i] == magic[j]) {
        num++;
        i++;
        j++;
    }

    if (num != len1 || !(message[i]) || message[i] == '\n') {
        return 0;
    }
    
    while (message[i] && message[i] == ' ') {
        i++;
        
        if (!(message[i]) || message[i] == '\n') {
            return 0;
        }
    }
    
    num = 0;
    j = 0;
    while (message[i] && message[i] == bye[j]) {
        num++;
        i++;
        j++;
    }
    
    if (num != len2 || !(message[i]) || message[i] == '\n') {
        return 0;
    }
    
    while (message[i] && message[i] == ' ') {
        i++;
        
        if (!(message[i]) || message[i] == '\n') {
            return 0;
        }
    }
    
    while (message[i] && message[i] != ' ') {
        if (message[i] < '0' && message[i] > '9') {
            return 0;
        }
        
        i++;
        
        if (!(message[i]) || message[i] == '\n') {
            return 0;
        }
    }
    
    while (message[i] && message[i] == ' ') {
        i++;
    }
    
    if (!(message[i]) || message[i] == '\n') {
        return 1;
    }
    return 0;
}