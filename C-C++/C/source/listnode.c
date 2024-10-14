#include "io_utils/io_utils.h"
#include <stdlib.h>

typedef struct ListNode {
  int value;
  struct ListNode *next;
} ListNode;

ListNode *CreateNode(int value) {
  ListNode *node = malloc(sizeof(ListNode));
  node->value = value;
  node->next = NULL;
  return node;
}

void DestroyNode(ListNode **node) {
  (*node)->next = NULL;
  free(*node);
  *node = NULL;
}

ListNode *CreateList(int array[], int length) {
  if (length <= 0) return NULL;

  ListNode *head = CreateNode(array[0]);
  ListNode *current = head;
  for (int i = 1; i < length; ++i) {
    current->next = CreateNode(array[i]);
    current = current->next;
  }

  return head;
}

void DestroyList(ListNode **head) {
  if (!head || !(*head)) return;
  ListNode *current = *head;
  while (current) {
    ListNode *to_be_destroy = current;
    current = current->next;
    DestroyNode(&to_be_destroy);
  }
  *head = NULL;
}

ListNode *FindNode(ListNode *head, int value) {
  while (head && head->value != value) {
    head = head->next;
  }
  return head;
}

void DeleteNode(ListNode **head, int value) {
  printf("Delete: %d\n", value);
  if (!head || !(*head)) return;
  if ((*head)->value == value) {
    ListNode *new_head = (*head)->next;
    DestroyNode(head);
    *head = new_head;
    return;
  }
  ListNode *current_node = *head;
  while (current_node->next && current_node->next->value != value) {
    current_node = current_node->next;
  }
  if (current_node->next) {
    ListNode *node_to_delete = current_node->next;
    current_node->next = current_node->next->next;
    DestroyNode(&node_to_delete);
  }
}

void InsertNode(ListNode **head, int value, int index) {
  printf("Insert: %d at %d\n", value, index);
  if (!head || index < 0) return;
  ListNode *new_node = CreateNode(value);
  if (index == 0) {
    new_node->next = *head;
    *head = new_node;
  } else {
    ListNode *current = *head;
    while (index > 1) {
      if (!current->next) {
        current->next = CreateNode(0);
      }
      current = current->next;
      index--;
    }

    new_node->next = current->next;
    current->next = new_node;
  }
}

void PrintList(ListNode *head) {
  while (head) {
    printf("%d, ", head->value);
    head = head->next;
  }
  printf("\n");
}

int main() {
  int array[] = {0, 1, 2, 3, 4};
  ListNode *head = CreateList(array, 5);
  PrintList(head);
  DeleteNode(&head, 0);
  PrintList(head);
  InsertNode(&head, 100, 10);
  PrintList(head);
  InsertNode(&head, 200, 0);
  PrintList(head);
  InsertNode(&head, 300, 8);
  PrintList(head);
  DeleteNode(&head, 2);
  PrintList(head);
  DeleteNode(&head, 10);
  PrintList(head);
  DestroyList(&head);
  return 0;
}
