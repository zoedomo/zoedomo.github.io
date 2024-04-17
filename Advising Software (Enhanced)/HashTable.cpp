// Zoe Domagalski
// Advisng Software
// Updated: enhancement two

#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <algorithm>
#include <map>
#include <queue>
#include <unordered_map>
#include <unordered_set>
#include <stack>
#include <cstdlib> // For rand() and srand()
#include <ctime>   // For time()

using namespace std;

// Course class to hold course info
class Course {
public:
    string courseNumber;
    string name;
    vector<string> prerequisites;

    Course() = default; // default constructor

    Course(const string& number, const string& courseName, const vector<string>& prereqs)
        : courseNumber(number), name(courseName), prerequisites(prereqs) {}
};

// Graph data structure to represent courses and their prerequisites
class Graph {
private:
    unordered_map<string, vector<string>> adjacencyList;

public:
    void addEdge(const string& source, const string& destination) {
        adjacencyList[source].push_back(destination);
    }

    unordered_map<string, vector<string>>& getAdjacencyList() {
        return adjacencyList;
    }
};

// Function to perform topological sorting using DFS
void topologicalSortUtil(const string& vertex, unordered_map<string, vector<string>>& adjacencyList,
    unordered_set<string>& visited, stack<string>& stack) {
    visited.insert(vertex);

    for (const auto& neighbor : adjacencyList[vertex]) {
        if (visited.find(neighbor) == visited.end()) {
            topologicalSortUtil(neighbor, adjacencyList, visited, stack);
        }
    }

    stack.push(vertex);
}

// Function to perform topological sorting
vector<string> topologicalSort(Graph& graph) {
    stack<string> stack;
    unordered_set<string> visited;

    for (const auto& vertex : graph.getAdjacencyList()) {
        if (visited.find(vertex.first) == visited.end()) {
            topologicalSortUtil(vertex.first, graph.getAdjacencyList(), visited, stack);
        }
    }

    vector<string> result;
    while (!stack.empty()) {
        result.push_back(stack.top());
        stack.pop();
    }

    return result;
}

// hash table to store course objects
map<string, Course> courses;

// function to load data from file into data structure and build the graph
void loadDataStructure(const string& filename, Graph& graph) {
    ifstream file(filename);
    if (!file.is_open()) {
        cout << "Error opening file: " << filename << endl;
        return;
    }

    string line;
    while (getline(file, line)) { //read each line from the file

        istringstream iss(line);
        string courseNumber, courseName;
        vector<string> prerequisites;

        getline(iss, courseNumber, ','); // extract course number
        getline(iss, courseName, ','); // extract course name 

        string prerequisite;
        while (getline(iss, prerequisite, ',')) { // extract each prerequisite 
            prerequisites.push_back(prerequisite);
            // Add edge to the graph
            graph.addEdge(prerequisite, courseNumber);
        }

        Course course(courseNumber, courseName, prerequisites); // create a Course object
        courses[courseNumber] = course; //store in map
    }

    file.close();
    cout << "Data Structure loaded successfully." << endl;
}

// function to print course list in alphanumeric order
void printCourseList() {
    vector<Course> sortedCourses; // store sorted courses
    for (const auto& pair : courses) { // iterate over each course in the map
        sortedCourses.push_back(pair.second);
    }

    sort(sortedCourses.begin(), sortedCourses.end(),
        [](const Course& a, const Course& b) { return a.courseNumber < b.courseNumber; });

    cout << "Course List:" << endl;
    for (const auto& course : sortedCourses) {    // iterate over each sorted course
        cout << "Course Number: " << course.courseNumber << "\t Course Name: " << course.name << endl;
    }
}

//function to print course information and prerequisites
void printCourse() {
    string courseNumber;
    cout << "Enter the course number: ";
    cin >> courseNumber;

    if (courses.find(courseNumber) != courses.end()) { // check if course number exists
        const Course& course = courses[courseNumber];
        cout << "Course Number: " << course.courseNumber << "\t Course Name: " << course.name << endl;

        if (!course.prerequisites.empty()) { // check if there are prerequisites
            cout << "Prerequisites:" << endl;
            for (const auto& prerequisite : course.prerequisites) {
                cout << "- " << prerequisite << endl;
            }
        }
        else {
            cout << "No prerequisites." << endl;
        }
    }
    else {
        cout << "Course not found." << endl;
    }
}

// Function to generate random advice quotes
string generateRandomAdvice() {
    // Seed the random number generator with current time
    srand(static_cast<unsigned int>(time(nullptr)));

    // Array of advice quotes
    vector<string> adviceQuotes = {
        "Make sure you do all your homework early within the week.",
        "Time management is important.",
        "Stay organized to stay ahead.",
        "Don't forget to take breaks and recharge.",
        "Seek help from your peers and professors when needed.",
        "Stay focused and motivated.",
        "Keep a positive attitude towards learning.",
        "Set achievable goals for yourself.",
        "Embrace challenges as opportunities to learn and grow.",
        "Believe in yourself and your abilities."
    };

    // Generate a random index within the range of adviceQuotes
    int index = rand() % adviceQuotes.size();

    // Return the randomly selected advice quote
    return adviceQuotes[index];
}

// Function to generate course recommendations based on random advice
void generateRecommendations(const vector<string>& studentInterests, const vector<string>& academicHistory) {
    // Generate a random advice quote
    string advice = generateRandomAdvice();

    // Display the random advice quote
    cout << "Random Advice for the Semester:" << endl;
    cout << "- " << advice << endl;
}

// function to display menu and prompt for user input
int getMenuChoice() {
    int choice;
    cout << "\nWelcome to the course planner." << endl << endl;
    cout << "1. Load Data Structure." << endl;
    cout << "2. Print Course List." << endl;
    cout << "3. Print Course." << endl;
    cout << "4. Generate Advice for the Semester." << endl;
    cout << "5. Exit" << endl << endl;
    cout << "What would you like to do? " << endl;

    while (!(cin >> choice)) {
        cin.clear();
        cin.ignore(numeric_limits<streamsize>::max(), '\n'); // making sure user input matches data type to prevent infinite loop 
        cout << "Invalid input. Please enter a number from the menu: ";
    }

    return choice;
}

int main() {
    int choice;
    string filename;
    Graph graph; // Create a graph object to represent course prerequisites

    while (true) { // loop until program is exited
        choice = getMenuChoice(); // display menu

        switch (choice) {
        case 1: {
            cout << "Enter the filename: ";
            cin >> filename;
            loadDataStructure(filename, graph);
            break;
        }
        case 2: {
            printCourseList();
            break;
        }
        case 3: {
            printCourse();
            break;
        }
        case 4: {
            // Generate a random advice quote
            string advice = generateRandomAdvice();

            // Display the random advice quote
            cout << "Advisor's Advice:" << endl;
            cout << "- " << advice << endl;
            break;
        }

        case 5:
            cout << "Thank you for using the course planner!" << endl;
            return 0;   // exit the program
        default:
            cout << "Invalid option." << endl;
        }

        // Clear input buffer
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
    }

    return 0;
}
