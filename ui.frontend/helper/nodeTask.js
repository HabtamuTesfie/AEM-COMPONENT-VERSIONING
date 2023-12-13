//------------------------------------------------------------------------------
/*   Application Name: Join KP - Doctors & Locations Search
 *       Date Created: 01/09/2017
 *           Compiler: TypeScript 2.0
 *
 *     Change History:
 *      Date            Programmer      Description/Comments/DefectID
 */
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
  /**
   * A module that provides an organized way of running syncronous tasks.
   * This module is explicitly syncronous! it's intended to order simple 
   * and cheap operations.. please do not use this for memory/cpu intensive ops.
   * the point here, is to give some structure to nodejs scripts without adding
   * a task runner dependency.
   */
//------------------------------------------------------------------------------
//--------------------------------------------------------------- module exports
exports.Task = Task;

//-------------------------------------------------------------- private helpers
const hash         = '#';
const space        = ' ';
const nodeTaskText = 'NODE TASK';
const stepText     = 'STEP';
const doneText     = 'DONE';
var indent = (n, str) => (str) ? hash.repeat(n)+ space + str : '';
var log = (str) => console.log(str);


//------------------------------------------------------------------------------
/**
 * Task prototype
 * @param taskDesc description of the task
 */
//------------------------------------------------------------------------------
function Task(taskDesc)
{
  log('###########');
  log(`${nodeTaskText}: ${taskDesc}`);
  log('');
}

//------------------------------------------------------------------------------
/**
 * Task step
 * @param stepText description of the step
 * @param stepFunction a function to be invoked as part of the step
 */
//------------------------------------------------------------------------------
Task.prototype.step = (stepDesc, stepFunction) =>
{
  //--------------------------------------------- indent and log step description
  var indentedStepText = indent(2, `${stepText}: ${stepDesc}`);
  log(indentedStepText);
  //------------------------------------------------------- invoke step function
  stepFunction();
}
//------------------------------------------------------------------------------
/**
 * Task done - adds ending log statements
 * @param stepText description of the step
 * @param stepFunction a function to be invoked as part of the step
 */
//------------------------------------------------------------------------------
Task.prototype.done = () => {
  log(`${nodeTaskText}: ${doneText}`)
  log('###########');
  log('');
}