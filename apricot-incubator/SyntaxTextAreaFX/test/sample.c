
// 
// REMARKS: To build a test run for the suggesting engine
//
//-----------------------------------------


#include <stdio.h>
#include <assert.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include "table.h"
#include "engine.h"
#include "suggest.h"

//-------------------------------------------------------------------------------------
// CONSTANTS and TYPES
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
// VARIABLES
//-------------------------------------------------------------------------------------

static int testsPassed = 0;
static int testsFailed = 0;
static int currentSize = 0;

//-------------------------------------------------------------------------------------
// PROTOTYPES
//-------------------------------------------------------------------------------------

void testFileProcessing();

//-------------------------------------------------------------------------------------
// FUNCTIONS
//-------------------------------------------------------------------------------------


int main( int argc, char *argv[] )
{
  printf( "Beginning tests...\n\n" );
  testFileProcessing();
  printf( "\nTests done.....\n" );
  
  return EXIT_SUCCESS;
}

//this function will test boundary conditions for a list of size one

void testFileProcessing()
{
  char *testString = "Neville";
  char fileName = "test.txt";

  if(processFile(filename, testString) != NULL)
  {
      printf("test passed");
      testsPassed++;
  }

  else
  {
    printf("test failed");
    testsFailed++;   
  }
}
