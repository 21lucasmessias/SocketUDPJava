#include "stdio.h"
#include "unistd.h"
#include "stdlib.h"
#include "pthread.h"
#include "semaphore.h"

#define N 5 

sem_t tCustomer; 
sem_t tBarber;	  
sem_t ttMutex;		

int waiting = 0;

void *barber(void *arg);
void *customer(void *arg);
void cut_hair();
void customer_arrived();
void get_haircut();
void giveup_haircut();

int main() {
	sem_init(&tCustomer, 1, 0);
	sem_init(&tBarber, 1, 0);
	sem_init(&tMutex, 1, 1);

	pthread_t b, c;

	pthread_create(&b, NULL, (void *)barber, NULL);

	while (1)
	{
		pthread_create(&c, NULL, (void *)customer, NULL);
		sleep(1);
	}

	return 0;
}

void *barber(void *arg) {
	while (1)
	{
		sem_wait(&tCustomer);	 /* vai dormir se o número de clientes for 0 */
		sem_wait(&tMutex);		 /* obtém acesso a alteração */
		waiting = waiting - 1; /*descresce de um o contador de clientes à espera */
		sem_post(&tBarber);		 /* um barbeiro está agora pronto para cortar cabelo */
		sem_post(&tMutex);		 /* libera alteração */
		cut_hair();						 /* corta o cabelo */
	}

	pthread_exit(NULL);
}

void *customer(void *arg)
{
	sem_wait(&tMutex); /* obtém acesso a alteração */

	if (waiting < N) { /* se não houver cadeiras vazias, saia */
		customer_arrived();
		waiting = waiting + 1; /* incrementa o contador de clientes à espera */
		sem_post(&tCustomer);	 /* acorda o barbeiro se necessário */
		sem_post(&tMutex);		 /* libera alteração */
		sem_wait(&tBarber);		 /* vai dormir se o número de barbeiros livres for 0 */
		get_haircut();				 /* sentado e sendo servido */
	}	else{
		sem_post(&tMutex); /* libera alteração */
		giveup_haircut();
	}

	pthread_exit(NULL);
}

void cut_hair() {
	printf("Barbeiro estah cortando o cabelo de alguem!\n");
	sleep(3);
}

void customer_arrived() {
	printf("Cliente chegou para cortar cabelo!\n");
}

void get_haircut() {
	printf("Cliente estah tendo o cabelo cortado!\n");
}

void giveup_haircut() {
	printf("Cliente desistiu! (O salao estah muito cheio!)\n");
}